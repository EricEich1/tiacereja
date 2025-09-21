export interface Usuario {
  id?: number;
  email: string;
  senha: string;
  nome?: string;
  role?: string;
}

export interface LoginRequest {
  email: string;
  senha: string;
}

export interface LoginResponse {
  token?: string;
  usuario: Usuario;
  message?: string;
}
