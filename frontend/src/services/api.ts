import axios, { AxiosError } from 'axios';
import { LoginCredentials, RegisterData, User } from '../types/auth';

// Use a default API URL if the environment variable is not set
const API_URL = 'http://localhost:8080/api';

// Create axios instance with default config
const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add request interceptor to add auth token to requests
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Add response interceptor to handle auth errors
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      // Clear token and redirect to login if unauthorized
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// Auth API
export const authApi = {
  login: async (username: string, password: string) => {
    const response = await api.post('/auth/login', { username, password });
    return response.data;
  },
  logout: () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    window.location.href = '/login';
  },
  getCurrentUser: () => {
    const userStr = localStorage.getItem('user');
    if (!userStr) return null;
    try {
      return JSON.parse(userStr);
    } catch (error) {
      console.error('Error parsing user from localStorage:', error);
      localStorage.removeItem('user'); // Clear invalid data
      return null;
    }
  },
  isAuthenticated: () => {
    return !!localStorage.getItem('token');
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
    const response = await api.delete(`/tournaments/${id}`);
    return response.data;
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
  test: async () => {
    console.log('Making GET request to /participants/test');
    try {
      const response = await api.get('/participants/test');
      console.log('Test response:', response);
      return response.data;
    } catch (error) {
      console.error('Error in test endpoint:', error);
      if (error instanceof AxiosError) {
        console.error('Error response data:', error.response?.data);
        console.error('Error response status:', error.response?.status);
      }
      throw error;
    }
  },
  
  getAll: async () => {
    console.log('Making GET request to /participants');
    try {
      const response = await api.get('/participants');
      console.log('Response from /participants:', response);
      console.log('Response data:', response.data);
      console.log('Response status:', response.status);
      console.log('Response headers:', response.headers);
      return response.data;
    } catch (error) {
      console.error('Error in GET /participants:', error);
      if (error instanceof AxiosError) {
        console.error('Error response data:', error.response?.data);
        console.error('Error response status:', error.response?.status);
        console.error('Error response headers:', error.response?.headers);
        console.error('Error config:', error.config);
      }
      throw error;
    }
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
    const response = await api.delete(`/participants/${id}`);
    return response.data;
  },

  getByClub: async (clubId: number) => {
    const response = await api.get(`/participants/club/${clubId}`);
    return response.data;
  },

  registerForTournament: async (participantId: number, tournamentId: number) => {
    const response = await api.post(`/participants/${participantId}/tournaments/${tournamentId}`);
    return response.data;
  },

  unregisterFromTournament: async (participantId: number, tournamentId: number) => {
    const response = await api.delete(`/participants/${participantId}/tournaments/${tournamentId}`);
    return response.data;
  },
};

// Clubs API
export const clubsApi = {
  test: async () => {
    console.log('Making GET request to /clubs/test');
    try {
      const response = await api.get('/clubs/test');
      console.log('Test response for clubs:', response);
      return response.data;
    } catch (error) {
      console.error('Error in clubs test endpoint:', error);
      if (error instanceof AxiosError) {
        console.error('Error response data:', error.response?.data);
        console.error('Error response status:', error.response?.status);
      }
      throw error;
    }
  },
  
  getAll: async () => {
    console.log('Making GET request to /clubs');
    try {
      const response = await api.get('/clubs');
      console.log('Response from /clubs:', response);
      return response.data;
    } catch (error) {
      console.error('Error in GET /clubs:', error);
      if (error instanceof AxiosError) {
        console.error('Error response data:', error.response?.data);
        console.error('Error response status:', error.response?.status);
      }
      throw error;
    }
  },

  getById: async (id: number) => {
    const response = await api.get(`/clubs/${id}`);
    return response.data;
  },

  create: async (clubData: any) => {
    const response = await api.post('/clubs', clubData);
    return response.data;
  },

  update: async (id: number, clubData: any) => {
    const response = await api.put(`/clubs/${id}`, clubData);
    return response.data;
  },

  delete: async (id: number) => {
    const response = await api.delete(`/clubs/${id}`);
    return response.data;
  },
}; 