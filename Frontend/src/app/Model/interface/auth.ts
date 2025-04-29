export interface AuthResponse {
  code: number;
  message: string;
  status: boolean;
  data: string;
}

export interface Register {
  userEmail: string;
  userPassword: string;
  userName: string;
  userSurname: string;
}
