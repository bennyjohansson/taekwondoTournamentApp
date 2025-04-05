import React, { useState, useEffect } from 'react';
import {
  Box,
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  TextField,
  Typography,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  IconButton,
  MenuItem,
  FormControl,
  InputLabel,
  Select,
} from '@mui/material';
import { Add as AddIcon, Edit as EditIcon, Delete as DeleteIcon, Remove as RemoveIcon } from '@mui/icons-material';
import { participantsApi, clubsApi, tournamentsApi } from '../services/api';

interface Participant {
  id: number;
  name: string;
  age: number;
  gender: string;
  skillLevel: string;
  club?: Club;
  tournamentId?: number;
  tournaments?: Tournament[];
}

interface Club {
  id: number;
  name: string;
}

interface Tournament {
  id: number;
  name: string;
}

export const ParticipantsPage: React.FC = () => {
  const [participants, setParticipants] = useState<Participant[]>([]);
  const [clubs, setClubs] = useState<Club[]>([]);
  const [tournaments, setTournaments] = useState<Tournament[]>([]);
  const [open, setOpen] = useState(false);
  const [editingParticipant, setEditingParticipant] = useState<Participant | null>(null);
  const [formData, setFormData] = useState<Omit<Participant, 'id'>>({
    name: '',
    age: 0,
    gender: '',
    skillLevel: '',
    club: undefined,
    tournamentId: undefined,
  });
  const [openTournamentDialog, setOpenTournamentDialog] = useState(false);
  const [selectedParticipant, setSelectedParticipant] = useState<Participant | null>(null);
  const [selectedTournamentId, setSelectedTournamentId] = useState<number | undefined>(undefined);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      // Test the API connections first
      console.log('Testing API connections...');
      const participantsTestResult = await participantsApi.test();
      console.log('Participants test result:', participantsTestResult);
      
      const clubsTestResult = await clubsApi.test();
      console.log('Clubs test result:', clubsTestResult);
      
      // If tests are successful, load the data
      console.log('Loading data...');
      const [participantsData, clubsData, tournamentsData] = await Promise.all([
        participantsApi.getAll(),
        clubsApi.getAll(),
        tournamentsApi.getAll(),
      ]);
      
      console.log('Participants data:', participantsData);
      console.log('Clubs data:', clubsData);
      console.log('Tournaments data:', tournamentsData);
      
      // Ensure each participant has the tournaments property
      const participantsWithTournaments = participantsData.map((participant: Participant) => ({
        ...participant,
        tournaments: participant.tournaments || []
      }));
      
      console.log('Participants with tournaments:', participantsWithTournaments);
      
      setParticipants(participantsWithTournaments);
      setClubs(clubsData);
      setTournaments(tournamentsData);
    } catch (error) {
      console.error('Error loading data:', error);
    }
  };

  const handleOpen = (participant?: Participant) => {
    if (participant) {
      setEditingParticipant(participant);
      setFormData(participant);
    } else {
      setEditingParticipant(null);
      setFormData({
        name: '',
        age: 0,
        gender: '',
        skillLevel: '',
        club: undefined,
        tournamentId: undefined,
      });
    }
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
    setEditingParticipant(null);
    setFormData({
      name: '',
      age: 0,
      gender: '',
      skillLevel: '',
      club: undefined,
      tournamentId: undefined,
    });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const participantData = {
        name: formData.name,
        age: formData.age,
        gender: formData.gender,
        skillLevel: formData.skillLevel,
        clubId: formData.club?.id
      };
      
      if (editingParticipant?.id) {
        await participantsApi.update(editingParticipant.id, participantData);
      } else {
        await participantsApi.create(participantData);
      }
      handleClose();
      loadData();
    } catch (error) {
      console.error('Error saving participant:', error);
    }
  };

  const handleDelete = async (id: number) => {
    if (window.confirm('Are you sure you want to delete this participant?')) {
      try {
        await participantsApi.delete(id);
        loadData();
      } catch (error) {
        console.error('Error deleting participant:', error);
      }
    }
  };

  const getClubName = (clubId: number) => {
    return clubs.find(club => club.id === clubId)?.name || 'Unknown Club';
  };

  const getTournamentName = (tournamentId: number) => {
    return tournaments.find(tournament => tournament.id === tournamentId)?.name || 'Unknown Tournament';
  };

  const handleRegisterForTournament = async (participantId: number, tournamentId: number) => {
    try {
      // Check if the participant is already registered for this tournament
      const participant = participants.find((p: Participant) => p.id === participantId);
      if (participant && participant.tournaments && participant.tournaments.some(t => t.id === tournamentId)) {
        console.log(`Participant ${participantId} is already registered for tournament ${tournamentId}`);
        return;
      }
      
      console.log(`Registering participant ${participantId} for tournament ${tournamentId}`);
      const response = await participantsApi.registerForTournament(participantId, tournamentId);
      console.log('Registration response:', response);
      setParticipants(prevParticipants => 
        prevParticipants.map(p => {
          if (p.id === participantId) {
            return {
              ...p,
              tournaments: [...(p.tournaments || []), { id: tournamentId, name: getTournamentName(tournamentId) }]
            };
          }
          return p;
        })
      );
    } catch (error) {
      console.error('Error registering for tournament:', error);
    }
  };

  const handleUnregisterFromTournament = async (participantId: number, tournamentId: number) => {
    try {
      // Check if the participant is registered for this tournament
      const participant = participants.find((p: Participant) => p.id === participantId);
      if (!participant || !participant.tournaments || !participant.tournaments.some(t => t.id === tournamentId)) {
        console.log(`Participant ${participantId} is not registered for tournament ${tournamentId}`);
        return;
      }
      
      console.log(`Unregistering participant ${participantId} from tournament ${tournamentId}`);
      const response = await participantsApi.unregisterFromTournament(participantId, tournamentId);
      console.log('Unregistration response:', response);
      setParticipants(prevParticipants => 
        prevParticipants.map(p => {
          if (p.id === participantId) {
            return {
              ...p,
              tournaments: (p.tournaments || []).filter(t => t.id !== tournamentId)
            };
          }
          return p;
        })
      );
    } catch (error) {
      console.error('Error unregistering from tournament:', error);
    }
  };

  return (
    <Box sx={{ p: 3 }}>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 3 }}>
        <Typography variant="h4">Participants</Typography>
        <Button
          variant="contained"
          startIcon={<AddIcon />}
          onClick={() => handleOpen()}
        >
          Add Participant
        </Button>
      </Box>

      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Name</TableCell>
              <TableCell>Age</TableCell>
              <TableCell>Gender</TableCell>
              <TableCell>Skill Level</TableCell>
              <TableCell>Club</TableCell>
              <TableCell>Tournaments</TableCell>
              <TableCell align="right">Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {participants.map((participant) => (
              <TableRow key={participant.id}>
                <TableCell>{participant.name}</TableCell>
                <TableCell>{participant.age}</TableCell>
                <TableCell>{participant.gender}</TableCell>
                <TableCell>{participant.skillLevel}</TableCell>
                <TableCell>{participant.club?.name || 'Unknown Club'}</TableCell>
                <TableCell>
                  <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1 }}>
                    {participant.tournaments?.map((tournament) => (
                      <Box key={tournament.id} sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                        <Typography>{tournament.name}</Typography>
                        <IconButton
                          size="small"
                          onClick={() => handleUnregisterFromTournament(participant.id, tournament.id)}
                        >
                          <RemoveIcon />
                        </IconButton>
                      </Box>
                    ))}
                    <Button
                      size="small"
                      startIcon={<AddIcon />}
                      onClick={() => {
                        setSelectedParticipant(participant);
                        setOpenTournamentDialog(true);
                      }}
                    >
                      Register for Tournament
                    </Button>
                  </Box>
                </TableCell>
                <TableCell align="right">
                  <IconButton onClick={() => handleOpen(participant)}>
                    <EditIcon />
                  </IconButton>
                  <IconButton onClick={() => handleDelete(participant.id)}>
                    <DeleteIcon />
                  </IconButton>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      <Dialog open={open} onClose={handleClose}>
        <DialogTitle>{editingParticipant ? 'Edit Participant' : 'Add Participant'}</DialogTitle>
        <DialogContent>
          <TextField
            autoFocus
            margin="dense"
            label="Name"
            fullWidth
            value={formData.name}
            onChange={(e) => setFormData({ ...formData, name: e.target.value })}
          />
          <TextField
            margin="dense"
            label="Age"
            type="number"
            fullWidth
            InputProps={{ inputProps: { min: 0 } }}
            value={formData.age}
            onChange={(e) => setFormData({ ...formData, age: parseInt(e.target.value) })}
          />
          <FormControl fullWidth margin="dense">
            <InputLabel>Gender</InputLabel>
            <Select
              value={formData.gender}
              label="Gender"
              onChange={(e) => setFormData({ ...formData, gender: e.target.value })}
            >
              <MenuItem value="Male">Male</MenuItem>
              <MenuItem value="Female">Female</MenuItem>
            </Select>
          </FormControl>
          <FormControl fullWidth margin="dense">
            <InputLabel>Skill Level</InputLabel>
            <Select
              value={formData.skillLevel}
              label="Skill Level"
              onChange={(e) => setFormData({ ...formData, skillLevel: e.target.value })}
            >
              <MenuItem value="WHITE_BELT">White Belt</MenuItem>
              <MenuItem value="YELLOW_BELT">Yellow Belt</MenuItem>
              <MenuItem value="GREEN_BELT">Green Belt</MenuItem>
              <MenuItem value="BLUE_BELT">Blue Belt</MenuItem>
              <MenuItem value="RED_BELT">Red Belt</MenuItem>
              <MenuItem value="BLACK_BELT">Black Belt</MenuItem>
            </Select>
          </FormControl>
          <FormControl fullWidth>
            <InputLabel>Club</InputLabel>
            <Select
              value={formData.club?.id || ''}
              onChange={(e) => {
                const selectedClub = clubs.find(club => club.id === e.target.value);
                setFormData({ ...formData, club: selectedClub || undefined });
              }}
            >
              {clubs.map((club) => (
                <MenuItem key={club.id} value={club.id}>
                  {club.name}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose}>Cancel</Button>
          <Button onClick={handleSubmit} variant="contained">
            {editingParticipant ? 'Update' : 'Create'}
          </Button>
        </DialogActions>
      </Dialog>

      <Dialog open={openTournamentDialog} onClose={() => setOpenTournamentDialog(false)}>
        <DialogTitle>Register for Tournament</DialogTitle>
        <DialogContent>
          <FormControl fullWidth sx={{ mt: 2 }}>
            <InputLabel>Tournament</InputLabel>
            <Select
              value={selectedTournamentId || ''}
              onChange={(e) => setSelectedTournamentId(e.target.value as number)}
            >
              {tournaments.map((tournament) => (
                <MenuItem key={tournament.id} value={tournament.id}>
                  {tournament.name}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenTournamentDialog(false)}>Cancel</Button>
          <Button
            onClick={() => {
              if (selectedParticipant && selectedTournamentId) {
                handleRegisterForTournament(selectedParticipant.id, selectedTournamentId);
                setOpenTournamentDialog(false);
              }
            }}
          >
            Register
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
}; 