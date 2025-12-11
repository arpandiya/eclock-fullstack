import { createContext } from 'react';

type User = {
  username: string;
  email: string;
  roles: string[];
} | null;

type AuthContextType = {
  user: User;
  loading: boolean;
  login: (username: string, password: string) => Promise<void>;
  logout: () => Promise<void>;
  refetch: () => Promise<void>;
} | null;

const AuthContext = createContext<AuthContextType>(null);

export default AuthContext;