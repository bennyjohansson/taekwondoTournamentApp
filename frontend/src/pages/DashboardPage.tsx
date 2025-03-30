import React from 'react';
import {
  Box,
  Card,
  CardContent,
  Typography,
  List,
  ListItem,
  ListItemText,
  ListItemIcon,
} from '@mui/material';
import {
  People,
  EmojiEvents,
  SportsKabaddi,
  Person,
} from '@mui/icons-material';
import { useAuth } from '../contexts/AuthContext';
import { UserRole } from '../types/auth';

export const DashboardPage: React.FC = () => {
  const { user } = useAuth();

  const getDashboardContent = () => {
    if (!user) return null;

    switch (user.role) {
      case UserRole.ADMIN:
        return (
          <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 3 }}>
            <Box sx={{ flex: { xs: '1 1 100%', md: '1 1 calc(50% - 12px)' } }}>
              <Card>
                <CardContent>
                  <Typography variant="h6" gutterBottom>
                    System Overview
                  </Typography>
                  <List>
                    <ListItem>
                      <ListItemIcon>
                        <People />
                      </ListItemIcon>
                      <ListItemText
                        primary="Total Users"
                        secondary="Manage all users in the system"
                      />
                    </ListItem>
                    <ListItem>
                      <ListItemIcon>
                        <EmojiEvents />
                      </ListItemIcon>
                      <ListItemText
                        primary="Active Tournaments"
                        secondary="View and manage all tournaments"
                      />
                    </ListItem>
                    <ListItem>
                      <ListItemIcon>
                        <SportsKabaddi />
                      </ListItemIcon>
                      <ListItemText
                        primary="Active Matches"
                        secondary="Monitor and manage ongoing matches"
                      />
                    </ListItem>
                  </List>
                </CardContent>
              </Card>
            </Box>
          </Box>
        );

      case UserRole.CLUB:
        return (
          <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 3 }}>
            <Box sx={{ flex: { xs: '1 1 100%', md: '1 1 calc(50% - 12px)' } }}>
              <Card>
                <CardContent>
                  <Typography variant="h6" gutterBottom>
                    Club Dashboard
                  </Typography>
                  <List>
                    <ListItem>
                      <ListItemIcon>
                        <People />
                      </ListItemIcon>
                      <ListItemText
                        primary="Club Participants"
                        secondary="Manage your club's participants"
                      />
                    </ListItem>
                    <ListItem>
                      <ListItemIcon>
                        <EmojiEvents />
                      </ListItemIcon>
                      <ListItemText
                        primary="Upcoming Tournaments"
                        secondary="View and register for tournaments"
                      />
                    </ListItem>
                  </List>
                </CardContent>
              </Card>
            </Box>
          </Box>
        );

      case UserRole.USER:
        return (
          <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 3 }}>
            <Box sx={{ flex: { xs: '1 1 100%', md: '1 1 calc(50% - 12px)' } }}>
              <Card>
                <CardContent>
                  <Typography variant="h6" gutterBottom>
                    My Dashboard
                  </Typography>
                  <List>
                    <ListItem>
                      <ListItemIcon>
                        <Person />
                      </ListItemIcon>
                      <ListItemText
                        primary="My Profile"
                        secondary="View and update your profile"
                      />
                    </ListItem>
                    <ListItem>
                      <ListItemIcon>
                        <SportsKabaddi />
                      </ListItemIcon>
                      <ListItemText
                        primary="My Matches"
                        secondary="View your upcoming and past matches"
                      />
                    </ListItem>
                  </List>
                </CardContent>
              </Card>
            </Box>
          </Box>
        );

      default:
        return (
          <Typography variant="h6" color="error">
            Invalid user role
          </Typography>
        );
    }
  };

  return (
    <Box>
      <Typography variant="h4" gutterBottom>
        Welcome, {user?.username}!
      </Typography>
      {getDashboardContent()}
    </Box>
  );
}; 