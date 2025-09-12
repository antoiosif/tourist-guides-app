export interface GuideFilters {
  page: number | null;
  pageSize: number | null;
  sortBy: string | null;
  sortDirection: string | null;
  isActive: boolean | null;
  uuid: string | null;
  recordNumber: string | null;
  userUsername: string | null | undefined;
  userFirstname: string | null;
  userLastname: string | null;
  regionId: bigint | null;
  languageId: bigint | null;
}

export interface VisitorFilters {
  page: number | null;
  pageSize: number | null;
  sortBy: string | null;
  sortDirection: string | null;
  isActive: boolean | null;
  uuid: string | null;
  userUsername: string | null | undefined;
  userFirstname: string | null;
  userLastname: string | null;
  regionId: bigint | null;
}

export interface ActivityFilters {
  page: number | null;
  pageSize: number | null;
  sortBy: string | null;
  sortDirection: string | null;
  uuid: string | null;
  title: string | null;
  status: string | null;
  categoryId: bigint | null;
}
