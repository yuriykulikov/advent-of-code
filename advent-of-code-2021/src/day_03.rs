use std::ops::BitXor;

fn gamma_and_epsilon(input: &str) -> (u32, u32) {
    let parsed: Vec<String> = input.lines().into_iter().map(|it| it.to_owned()).collect();
    let line_length = parsed.first().unwrap().len();

    let gamma_str: String = (0..line_length).into_iter().map(|pos| {
        most_popular_bit(&parsed, pos)
    }).collect();

    let gamma = u32::from_str_radix(gamma_str.as_str(), 2).unwrap();
    let mask = (0..line_length).fold(0 as u32, |acc, pos| acc + (1 << pos));
    let epsilon = gamma.bitxor(mask);
    (gamma, epsilon)
}

fn most_popular_bit(words: &[String], pos: usize) -> char {
    let with_1 = words.iter().filter(|it| it.chars().nth(pos) == Some('1')).count();
    let with_0 = words.len() - with_1;
    return if with_1 >= with_0 { '1' } else { '0' };
}

fn oxygen(input: &str) -> u32 {
    let mut remaining: Vec<String> = input.lines().into_iter().map(|it| it.to_owned()).collect();

    for pos in 0..remaining.first().unwrap().len() {
        let popular_bit = most_popular_bit(&remaining, pos);
        remaining.retain(|it| it.chars().nth(pos) == Some(popular_bit));
        if remaining.len() == 1 {
            break;
        }
    }

    u32::from_str_radix(remaining.first().unwrap().as_str(), 2).unwrap()
}

fn scrubber(input: &str) -> u32 {
    let mut remaining: Vec<String> = input.lines().into_iter().map(|it| it.to_owned()).collect();

    for pos in 0..remaining.first().unwrap().len() {
        let popular_bit = most_popular_bit(&remaining, pos);
        remaining.retain(|it| it.chars().nth(pos) != Some(popular_bit));
        if remaining.len() == 1 {
            break;
        }
    }

    u32::from_str_radix(remaining.first().unwrap().as_str(), 2).unwrap()
}

#[cfg(test)]
mod test {
    use crate::day_03::{gamma_and_epsilon, oxygen, scrubber};

    #[test]
    fn gamma_test() {
        assert_eq!(gamma_and_epsilon(include_str!("day_03_test_input")).0, 22);
    }

    #[test]
    fn epsilon_test() {
        assert_eq!(gamma_and_epsilon(include_str!("day_03_test_input")).1, 9);
    }

    #[test]
    fn silver_test() {
        let (gamma, epsilon) = gamma_and_epsilon(include_str!("day_03_test_input"));
        assert_eq!(gamma * epsilon, 198);
    }

    #[test]
    fn silver() {
        let (gamma, epsilon) = gamma_and_epsilon(include_str!("day_03_input"));
        assert_eq!(gamma * epsilon, 2724524);
    }

    #[test]
    fn oxygen_test() {
        assert_eq!(oxygen(include_str!("day_03_test_input")), 23);
    }

    #[test]
    fn scrubber_test() {
        assert_eq!(scrubber(include_str!("day_03_test_input")), 10);
    }

    #[test]
    fn gold_test() {
        assert_eq!(scrubber(include_str!("day_03_test_input")) * oxygen(include_str!("day_03_test_input")), 230);
    }

    #[test]
    fn gold() {
        assert_eq!(scrubber(include_str!("day_03_input")) * oxygen(include_str!("day_03_input")), 2775870);
    }
}