#[cfg(test)]
mod test {
    use std::collections::HashSet;

    use itertools::Itertools;

    fn count_unique_segments(input: Vec<(Vec<String>, Vec<String>)>) -> u32 {
        input
            .iter()
            .flat_map(|entry| (*entry).1.iter())
            .filter(|item| item.len() == 2 || item.len() == 3 || item.len() == 4 || item.len() == 7)
            .count() as u32
    }

    fn parse(input: &str) -> Vec<(Vec<String>, Vec<String>)> {
        input
            .lines()
            .map(|line| {
                let (front, back) = line.split_once("|").unwrap();
                (
                    front.split(" ").map(|it| it.trim().to_owned()).collect(),
                    back.trim()
                        .split(" ")
                        .map(|it| it.trim().to_owned())
                        .collect(),
                )
            })
            .collect()
    }

    /// true if one is completely overlapped by the other
    fn overlap(first: &String, second: &String) -> bool {
        first.chars().all(|char| second.contains(char))
            || second.chars().all(|char| first.contains(char))
    }

    fn read_7seg(input: Vec<(Vec<String>, Vec<String>)>) -> u32 {
        input
            .iter()
            .map(|(signals, display)| {
                let digits = decode_signals(signals);
                read_7seg_indicator(&digits, display)
            })
            .sum::<u32>()
    }

    fn read_7seg_indicator(signals: &Vec<String>, display: &Vec<String>) -> u32 {
        display
            .iter()
            .map(|digit| digit.chars().sorted().join(""))
            .zip((0..display.len()).rev())
            .map(|(digit, index)| {
                let parsed_digit = signals.iter().position(|it| *it == digit).unwrap() as u32;
                parsed_digit * 10_u32.pow(index as u32)
            })
            .sum::<u32>()
    }

    /// Vector of signals for each number (position)
    fn decode_signals(digits: &Vec<String>) -> Vec<String> {
        let digits: HashSet<String> = digits
            .iter()
            .map(|digit| digit.chars().sorted().join(""))
            .collect();

        let one = digits.iter().find(|it| it.len() == 2).unwrap();
        let seven = digits.iter().find(|it| it.len() == 3).unwrap();
        let four = digits.iter().find(|it| it.len() == 4).unwrap();
        let eight = digits.iter().find(|it| it.len() == 7).unwrap();
        let three = digits
            .iter()
            .find(|it| it.len() == 5 && overlap(it, one))
            .unwrap();
        let six = digits
            .iter()
            .find(|it| it.len() == 6 && !overlap(it, one))
            .unwrap();
        let nine = digits
            .iter()
            .find(|it| it.len() == 6 && overlap(it, three))
            .unwrap();
        let zero = digits
            .iter()
            .find(|it| it.len() == 6 && *it != six && *it != nine)
            .unwrap();
        let five = digits
            .iter()
            .find(|it| it.len() == 5 && *it != three && overlap(it, six))
            .unwrap();
        let two = digits
            .iter()
            .find(|it| it.len() == 5 && *it != three && !overlap(it, six))
            .unwrap();
        vec![zero, one, two, three, four, five, six, seven, eight, nine]
            .into_iter()
            .map(|it| it.to_owned())
            .collect()
    }

    #[test]
    fn silver_test() {
        assert_eq!(
            count_unique_segments(parse(include_str!("day_08_test_input"))),
            26
        );
    }

    #[test]
    fn silver() {
        assert_eq!(
            count_unique_segments(parse(include_str!("day_08_input"))),
            245
        );
    }

    #[test]
    fn small_gold_test() {
        assert_eq!(
            read_7seg(parse(include_str!("day_08_small_test_input"))),
            5353
        );
    }

    #[test]
    fn gold_test() {
        assert_eq!(read_7seg(parse(include_str!("day_08_test_input"))), 61229);
    }

    #[test]
    fn gold() {
        assert_eq!(read_7seg(parse(include_str!("day_08_input"))), 983026);
    }
}
