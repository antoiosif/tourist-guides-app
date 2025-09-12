import { User, UserInsertDTO, UserReadOnlyDTO, UserUpdateDTO } from "./user";

export interface Visitor {
  createdAt: string;
  updatedAt: string;
  id: bigint;
  isActive: boolean;
  uuid: string;
  user: User;
  regionId: bigint | null;
}

export interface VisitorInsertDTO {
  userInsertDTO: UserInsertDTO;
  regionId: bigint | null;
}

export interface VisitorUpdateDTO {
  id: bigint;
  isActive: boolean;
  uuid: string;
  userUpdateDTO: UserUpdateDTO;
  regionId: bigint | null;
}

export interface VisitorReadOnlyDTO {
  createdAt: string;
  updatedAt: string;
  id: bigint;
  isActive: boolean;
  uuid: string;
  userReadOnlyDTO: UserReadOnlyDTO;
  region: string | null;
}
