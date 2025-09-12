export interface User {
  createdAt: string;
  updatedAt: string;
  id: bigint;
  isActive: boolean;
  username: string;
  password: string;
  firstname: string;
  lastname: string;
  gender: string | null;
  role: string;
}

export interface UserInsertDTO {
  username: string;
  password: string;
  firstname: string;
  lastname: string;
  gender: string | null;
}

export interface UserUpdateDTO {
  id: bigint;
  isActive: boolean;
  username: string;
  password: string;
  firstname: string;
  lastname: string;
  gender: string | null;
  role: string;
}

export interface UserReadOnlyDTO {
  createdAt: string;
  updatedAt: string;
  id: bigint;
  isActive: boolean;
  username: string;
  password: string;
  firstname: string;
  lastname: string;
  gender: string;
  role: string;
}

export interface Credentials {
  username: string;
  password: string;
}

export interface LoggedInUser {
  sub: string;    // username
  uuid: string;
  firstname: string;
  lastname: string;
  role: string;
}

export interface AuthenticationResponseDTO {
  token: string;
}
