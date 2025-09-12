export interface Activity {
  createdAt: string;
  updatedAt: string;
  id: bigint;
  uuid: string;
  title: string;
  description: string | null;
  dateTime: string;
  price: number;
  status: string;
  categoryId: bigint;
}

export interface ActivityInsertDTO {
  title: string;
  description: string | null;
  dateTime: string;
  price: number;
  status: string;
  categoryId: bigint;
}

export interface ActivityUpdateDTO {
  id: bigint;
  uuid: string;
  title: string;
  description: string | null;
  dateTime: string;
  price: number;
  status: string;
  categoryId: bigint;
}

export interface ActivityReadOnlyDTO {
  createdAt: string;
  updatedAt: string;
  id: bigint;
  uuid: string;
  title: string;
  description: string;
  dateTime: string;
  price: number;
  status: string;
  category: string;
}
