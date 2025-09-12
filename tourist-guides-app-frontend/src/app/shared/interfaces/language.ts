export interface Language {
  id: bigint;
  code: string;
  name: string;
}

export interface LanguageInsertDTO {
  code: string;
  name: string;
}

export interface LanguageUpdateDTO {
  id: bigint;
  code: string;
  name: string;
}

export interface LanguageReadOnlyDTO {
  id: bigint;
  code: string;
  name: string;
}
