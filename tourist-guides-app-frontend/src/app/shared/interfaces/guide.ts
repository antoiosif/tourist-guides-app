import { User, UserInsertDTO, UserReadOnlyDTO, UserUpdateDTO } from "./user";

export interface Guide {
  createdAt: string;
  updatedAt: string;
  id: bigint;
  isActive: boolean;
  uuid: string;
  recordNumber: string;
  dateOfIssue: string;
  phoneNumber: string | null;
  email: string | null;
  bio: string | null;
  user: User;
  regionId: bigint;
  languageId: bigint;
}

export interface GuideInsertDTO {
  recordNumber: string;
  dateOfIssue: string;
  phoneNumber: string | null;
  email: string | null;
  bio: string | null;
  userInsertDTO: UserInsertDTO;
  regionId: bigint;
  languageId: bigint;
}

export interface GuideUpdateDTO {
  id: bigint;
  isActive: boolean;
  uuid: string;
  recordNumber: string;
  dateOfIssue: string;
  phoneNumber: string | null;
  email: string | null;
  bio: string | null;
  userUpdateDTO: UserUpdateDTO;
  regionId: bigint;
  languageId: bigint;
}

export interface GuideReadOnlyDTO {
  createdAt: string;
  updatedAt: string;
  id: bigint;
  isActive: boolean;
  uuid: string;
  recordNumber: string;
  dateOfIssue: string;
  phoneNumber: string;
  email: string;
  bio: string;
  userReadOnlyDTO: UserReadOnlyDTO;
  region: string;
  language: string;
}
