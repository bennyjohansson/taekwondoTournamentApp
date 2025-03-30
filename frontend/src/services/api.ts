import axios from 'axios';
import { LoginCredentials, RegisterData, User } from '../types/auth';

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

// Create axios instance with default config
const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add token to requests if it exists
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Auth API
export const authApi = {
  login: async (credentials: LoginCredentials) => {
    const response = await api.post<{ token: string; user: User }>('/auth/login', credentials);
    return response.data;
  },

  register: async (userData: RegisterData) => {
    const response = await api.post<{ token: string; user: User }>('/auth/register', userData);
    return response.data;
  },

  validateToken: async () => {
    const response = await api.get<User>('/auth/validate');
    return response.data;
  },
};

// Users API
export const usersApi = {
  getAll: async () => {
    const response = await api.get<User[]>('/users');
    return response.data;
  },

  getById: async (id: number) => {
    const response = await api.get<User>(`/users/${id}`);
    return response.data;
  },

  update: async (id: number, userData: Partial<User>) => {
    const response = await api.put<User>(`/users/${id}`, userData);
    return response.data;
  },

  delete: async (id: number) => {
    await api.delete(`/users/${id}`);
  },
};

// Tournaments API
export const tournamentsApi = {
  getAll: async () => {
    const response = await api.get('/tournaments');
    return response.data;
  },

  getById: async (id: number) => {
    const response = await api.get(`/tournaments/${id}`);
    return response.data;
  },

  create: async (tournamentData: any) => {
    const response = await api.post('/tournaments', tournamentData);
    return response.data;
  },

  update: async (id: number, tournamentData: any) => {
    const response = await api.put(`/tournaments/${id}`, tournamentData);
    return response.data;
  },

  delete: async (id: number) => {
    await api.delete(`/tournaments/${id}`);
  },
};

// Matches API
export const matchesApi = {
  getAll: async () => {
    const response = await api.get('/matches');
    return response.data;
  },

  getById: async (id: number) => {
    const response = await api.get(`/matches/${id}`);
    return response.data;
  },

  create: async (matchData: any) => {
    const response = await api.post('/matches', matchData);
    return response.data;
  },

  update: async (id: number, matchData: any) => {
    const response = await api.put(`/matches/${id}`, matchData);
    return response.data;
  },

  delete: async (id: number) => {
    await api.delete(`/matches/${id}`);
  },

  getByTournament: async (tournamentId: number) => {
    const response = await api.get(`/matches/tournament/${tournamentId}`);
    return response.data;
  },

  getByParticipant: async (participantId: number) => {
    const response = await api.get(`/matches/participant/${participantId}`);
    return response.data;
  },
};

// Participants API
export const participantsApi = {
  getAll: async () => {
    const response = await api.get('/participants');
    return response.data;
  },

  getById: async (id: number) => {
    const response = await api.get(`/participants/${id}`);
    return response.data;
  },

  create: async (participantData: any) => {
    const response = await api.post('/participants', participantData);
    return response.data;
  },

  update: async (id: number, participantData: any) => {
    const response = await api.put(`/participants/${id}`, participantData);
    return response.data;
  },

  delete: async (id: number) => {
    await api.delete(`/participants/${id}`);
  },

  getByClub: async (clubId: number) => {
    const response = await api.get(`/participants/club/${clubId}`);
    return response.data;
  },
}; 