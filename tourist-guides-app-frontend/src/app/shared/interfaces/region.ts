export interface Region {
  id: bigint;
  name: string;
}

export interface RegionInsertDTO {
  name: string;
}

export interface RegionUpdateDTO {
  id: bigint;
  name: string;
}

export interface RegionReadOnlyDTO {
  id: bigint;
  name: string;
}
