import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { ThemeProvider, createTheme } from '@mui/material';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { AuthProvider } from './contexts/AuthContext';
import { ProtectedRoute } from './components/ProtectedRoute';
import { MainLayout } from './layouts/MainLayout';
import { LoginPage } from './pages/LoginPage';
import { DashboardPage } from './pages/DashboardPage';
import { UserRole } from './types/auth';

// Create a client
const queryClient = new QueryClient();

// Create theme
const theme = createTheme({
  palette: {
    primary: {
      main: '#1976d2',
    },
    secondary: {
      main: '#dc004e',
    },
  },
});

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <ThemeProvider theme={theme}>
        <AuthProvider>
          <Router>
            <Routes>
              {/* Public routes */}
              <Route path="/login" element={<LoginPage />} />
              <Route path="/register" element={<div>Register Page</div>} />
              <Route path="/unauthorized" element={<div>Unauthorized Access</div>} />

              {/* Protected routes */}
              <Route
                path="/"
                element={
                  <ProtectedRoute>
                    <MainLayout />
                  </ProtectedRoute>
                }
              >
                <Route index element={<Navigate to="/dashboard" replace />} />
                <Route path="dashboard" element={<DashboardPage />} />

                {/* Admin routes */}
                <Route
                  path="users"
                  element={
                    <ProtectedRoute allowedRoles={[UserRole.ADMIN]}>
                      <div>Users Management</div>
                    </ProtectedRoute>
                  }
                />
                <Route
                  path="tournaments"
                  element={
                    <ProtectedRoute allowedRoles={[UserRole.ADMIN, UserRole.CLUB]}>
                      <div>Tournaments</div>
                    </ProtectedRoute>
                  }
                />
                <Route
                  path="matches"
                  element={
                    <ProtectedRoute allowedRoles={[UserRole.ADMIN]}>
                      <div>Matches Management</div>
                    </ProtectedRoute>
                  }
                />

                {/* Club routes */}
                <Route
                  path="participants"
                  element={
                    <ProtectedRoute allowedRoles={[UserRole.CLUB]}>
                      <div>Participants Management</div>
                    </ProtectedRoute>
                  }
                />

                {/* User routes */}
                <Route
                  path="my-matches"
                  element={
                    <ProtectedRoute allowedRoles={[UserRole.USER]}>
                      <div>My Matches</div>
                    </ProtectedRoute>
                  }
                />
              </Route>
            </Routes>
          </Router>
        </AuthProvider>
      </ThemeProvider>
    </QueryClientProvider>
  );
}

export default App;
