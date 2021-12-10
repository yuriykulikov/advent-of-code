use std::str::FromStr;

use itertools::Itertools;

fn count_increases(input: &str) -> u32 {
    input
        .lines()
        .into_iter()
        .map(|line| u32::from_str(line).unwrap())
        .tuple_windows()
        .filter(|(p, n)| n > p)
        .count() as u32
}

fn count_window_increases(input: &str) -> u32 {
    input
        .lines()
        .into_iter()
        .map(|line| u32::from_str(line).unwrap())
        .tuple_windows::<(_, _, _)>()
        .map(|it| it.0 + it.1 + it.2)
        .tuple_windows()
        .filter(|(p, n)| n > p)
        .count() as u32
}

#[cfg(test)]
mod test {
    use crate::day_01::{count_increases, count_window_increases};

    #[test]
    fn silver_test() {
        assert_eq!(count_increases(include_str!("day_01_test_input")), 7);
    }

    #[test]
    fn silver() {
        assert_eq!(count_increases(include_str!("day_01_input")), 1616);
    }

    #[test]
    fn gold_test() {
        assert_eq!(count_window_increases(include_str!("day_01_test_input")), 5);
    }

    #[test]
    fn gold() {
        assert_eq!(count_window_increases(include_str!("day_01_input")), 1645);
    }
}
