export interface Paginated<T> {
  data: T[];
  totalElements: bigint;
  totalPages: number;
  numberOfElements: number;
  currentPage: number;
  pageSize: number;
}
