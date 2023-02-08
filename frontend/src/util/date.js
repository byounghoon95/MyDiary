//2023-01-01 형식의 날짜를 받아올 수 있음
export const getStringDate = (date) => {
    return date.toISOString().slice(0,10);
}