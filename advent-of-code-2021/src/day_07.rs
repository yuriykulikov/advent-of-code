fn min_required_fuel(crabs: Vec<i32>) -> i32 {
    (*crabs.iter().min().unwrap()..=*crabs.iter().max().unwrap())
        .map(|pos| {
            let fuel = crabs.iter().map(|crab| (*crab - pos).abs()).sum::<i32>();
            fuel
        })
        .min()
        .unwrap_or(0)
}

fn min_required_fuel_2(crabs: Vec<i32>) -> i32 {
    (*crabs.iter().min().unwrap()..=*crabs.iter().max().unwrap())
        .map(|pos| {
            let fuel = crabs
                .iter()
                .map(|crab| {
                    let steps = (*crab - pos).abs();
                    // Sum of an arithmetic progression
                    steps * (steps + 1) / 2
                })
                .sum::<i32>();
            fuel
        })
        .min()
        .unwrap_or(0)
}

#[cfg(test)]
mod test {
    use std::str::FromStr;

    use crate::day_07::{min_required_fuel, min_required_fuel_2};

    fn parse(input: &str) -> Vec<i32> {
        input
            .split(",")
            .map(|it| i32::from_str(it).unwrap())
            .collect()
    }

    #[test]
    fn silver_test() {
        assert_eq!(
            min_required_fuel(parse(include_str!("day_07_test_input"))),
            37
        );
    }

    #[test]
    fn silver() {
        assert_eq!(
            min_required_fuel(parse(include_str!("day_07_input"))),
            356179
        );
    }

    #[test]
    fn gold_test() {
        assert_eq!(
            min_required_fuel_2(parse(include_str!("day_07_test_input"))),
            168
        );
    }

    #[test]
    fn gold() {
        assert_eq!(
            min_required_fuel_2(parse(include_str!("day_07_input"))),
            99788435
        );
    }
}
