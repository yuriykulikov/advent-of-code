use std::str::FromStr;

struct Board {
    rows: Vec<Vec<u32>>,
    columns: Vec<Vec<u32>>,
}

fn parse(input: &str) -> (Vec<u32>, Vec<Board>) {
    let (seq_str, all_boards_str) = input.split_once("\n").unwrap();
    let seq = seq_str
        .split(",")
        .map(|it| u32::from_str(it).unwrap())
        .collect();

    let boards = all_boards_str
        .split("\n\n")
        .map(|board_str| {
            let rows: Vec<Vec<u32>> = board_str
                .lines()
                .into_iter()
                .filter(|it| !it.is_empty())
                .map(|line| {
                    line.split(char::is_whitespace)
                        .filter_map(|it| u32::from_str(it).ok())
                        .collect()
                })
                .collect();

            let mut columns = vec![];
            for i in 0..rows.len() {
                let column = rows.iter().map(|row| *row.get(i).unwrap()).collect();
                columns.push(column);
            }

            Board { rows, columns }
        })
        .collect();
    (seq, boards)
}

/// In all [boards], find the first Board which has at least one row or column
/// all elements of which are present in the [seq]. Return the board and the subsequence.
fn find_winning_board<'a>(seq: &'a Vec<u32>, boards: &'a Vec<Board>) -> (&'a Board, &'a [u32]) {
    let (winning_board, subsequence) = (0..seq.len())
        .map(move |length| seq.split_at(length).0)
        .filter_map(|subsequence| {
            boards
                .iter()
                .find(|board| {
                    // TODO there must be a simple way to check contains_all or intersect
                    let has_crossed_column = board
                        .columns
                        .iter()
                        .any(|column| column.iter().all(|e| subsequence.contains(e)));
                    let has_crossed_row = board
                        .rows
                        .iter()
                        .any(|row| row.iter().all(|e| subsequence.contains(e)));
                    has_crossed_column || has_crossed_row
                })
                .map(|board| (board, subsequence))
        })
        .next()
        .unwrap();
    (winning_board, subsequence)
}

fn find_last_winning_board<'a>(
    seq: &'a Vec<u32>,
    boards: &'a Vec<Board>,
) -> (&'a Board, &'a [u32]) {
    let all_won_subsequence = (0..seq.len())
        .map(move |length| seq.split_at(length).0)
        .filter(|subsequence| {
            boards.iter().all(|board| {
                // TODO there must be a simple way to check contains_all or intersect
                let has_crossed_column = board
                    .columns
                    .iter()
                    .any(|column| column.iter().all(|e| subsequence.contains(e)));
                let has_crossed_row = board
                    .rows
                    .iter()
                    .any(|row| row.iter().all(|e| subsequence.contains(e)));
                has_crossed_column || has_crossed_row
            })
        })
        .next()
        .unwrap();

    let prev_subsequence = all_won_subsequence.split_last().unwrap().1;

    let last_board_to_win = boards
        .iter()
        .find(|board| {
            // find the board which does not win with this sequence
            let has_crossed_column = board
                .columns
                .iter()
                .any(|column| column.iter().all(|e| prev_subsequence.contains(e)));
            let has_crossed_row = board
                .rows
                .iter()
                .any(|row| row.iter().all(|e| prev_subsequence.contains(e)));
            !has_crossed_column && !has_crossed_row
        })
        .unwrap();

    (last_board_to_win, all_won_subsequence)
}

fn calc_score(winning_board: &Board, subsequence: &[u32]) -> u32 {
    winning_board
        .rows
        .iter()
        .flatten()
        .filter(|n| !subsequence.contains(n))
        .sum::<u32>()
        * subsequence.last().unwrap()
}

#[cfg(test)]
mod test {
    use crate::day_04::{calc_score, find_last_winning_board, find_winning_board, parse};

    #[test]
    fn silver_test() {
        let (seq, boards) = parse(include_str!("day_04_test_input"));
        let (winning_board, subsequence) = find_winning_board(&seq, &boards);
        assert_eq!(calc_score(winning_board, subsequence), 4512);
    }

    #[test]
    fn silver() {
        let (seq, boards) = parse(include_str!("day_04_input"));
        let (winning_board, subsequence) = find_winning_board(&seq, &boards);
        assert_eq!(calc_score(winning_board, subsequence), 45031);
    }

    #[test]
    fn gold_test() {
        let (seq, boards) = parse(include_str!("day_04_test_input"));
        let (last_board, subsequence) = find_last_winning_board(&seq, &boards);
        assert_eq!(calc_score(last_board, subsequence), 1924);
    }

    #[test]
    fn gold() {
        let (seq, boards) = parse(include_str!("day_04_input"));
        let (last_board, subsequence) = find_last_winning_board(&seq, &boards);
        assert_eq!(calc_score(last_board, subsequence), 2568);
    }

    #[test]
    fn parsing_test() {
        let (seq, boards) = parse(include_str!("day_04_test_input"));
        assert_eq!(
            seq,
            vec![
                7, 4, 9, 5, 11, 17, 23, 2, 0, 14, 21, 24, 10, 16, 13, 6, 15, 25, 12, 22, 18, 20, 8,
                19, 3, 26, 1
            ]
        );
        assert_eq!(
            boards.first().unwrap().rows,
            vec![
                vec![22, 13, 17, 11, 0],
                vec![8, 2, 23, 4, 24],
                vec![21, 9, 14, 16, 7],
                vec![6, 10, 3, 18, 5],
                vec![1, 12, 20, 15, 19],
            ]
        );

        assert_eq!(
            boards.first().unwrap().columns,
            vec![
                vec![22, 8, 21, 6, 1],
                vec![13, 2, 9, 10, 12],
                vec![17, 23, 14, 3, 20],
                vec![11, 4, 16, 18, 15],
                vec![0, 24, 7, 5, 19],
            ]
        );
    }
}
