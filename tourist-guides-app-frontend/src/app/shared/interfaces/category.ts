export interface Category {
  id: bigint;
  name: string;
}

export interface CategoryInsertDTO {
  name: string;
}

export interface CategoryUpdateDTO {
  id: bigint;
  name: string;
}

export interface CategoryReadOnlyDTO {
  id: bigint;
  name: string;
}
