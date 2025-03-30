export enum UserRole {
  ADMIN = 'ADMIN',
  CLUB = 'CLUB',
  USER = 'USER'
}

export interface User {
  id?: number;
  username: string;
  email: string;
  password?: string; // Optional because we don't want to send passwords in responses
  role: UserRole;
}

export interface LoginCredentials {
  username: string;
  password: string;
}

export interface RegisterData extends Omit<User, 'id'> {
  // Same as User but without id and password is required
}

export interface AuthResponse {
  user: User;
  token: string;
}

export interface ErrorResponse {
  message: string;
}

export interface AuthState {
  user: User | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  error: string | null;
}

export interface AuthContextType extends AuthState {
  login: (credentials: LoginCredentials) => Promise<void>;
  logout: () => void;
  register: (userData: RegisterData) => Promise<void>;
} 