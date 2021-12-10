use std::collections::HashMap;
use std::str::FromStr;

#[derive(Debug, Copy, Clone, Eq, PartialEq, Hash)]
struct Key {
    fish: u32,
    days: u32,
}

struct Fishes {
    cache: HashMap<Key, u64>,
}

impl Fishes {
    pub(crate) fn new() -> Self {
        Fishes {
            cache: HashMap::new(),
        }
    }
}

/// Grown fish produces an offspring every 6 days
/// New fish becomes a grown fish within 2 days
impl Fishes {
    fn multiply(&mut self, fish: u32, days: u32) -> u64 {
        let key = Key { fish, days };
        if let Some(calculation) = self.cache.get(&key) {
            calculation.clone()
        } else if days == 0 {
            1 // finished, if no days is left, this fish won't produce any offspring
        } else if fish == 0 {
            // on this day the fish produces the offspring and resets the growth counter
            let child_descendants = self.multiply(8, days - 1);
            let own_descendants = self.multiply(6, days - 1);
            let descendants = own_descendants + child_descendants;
            self.cache.insert(key, descendants);
            descendants
        } else {
            self.multiply(fish - 1, days - 1)
        }
    }
}

fn parse(input: &str) -> Vec<u32> {
    input
        .split(",")
        .map(|it| u32::from_str(it).unwrap())
        .collect()
}

#[cfg(test)]
mod test {
    use crate::day_06::{parse, Fishes};

    #[test]
    fn silver_smaller_test() {
        let mut fishes = Fishes::new();
        let fish_count = parse(include_str!("day_06_test_input"))
            .iter()
            .map(|fish| fishes.multiply(*fish, 18))
            .sum::<u64>();
        assert_eq!(fish_count, 26)
    }

    #[test]
    fn silver_test() {
        let mut fishes = Fishes::new();
        let fish_count = parse(include_str!("day_06_test_input"))
            .iter()
            .map(|fish| fishes.multiply(*fish, 80))
            .sum::<u64>();
        assert_eq!(fish_count, 5934)
    }

    #[test]
    fn silver() {
        let mut fishes = Fishes::new();
        let fish_count = parse(include_str!("day_06_input"))
            .iter()
            .map(|fish| fishes.multiply(*fish, 80))
            .sum::<u64>();
        assert_eq!(fish_count, 359999)
    }

    #[test]
    fn gold_test() {
        let mut fishes = Fishes::new();
        let fish_count = parse(include_str!("day_06_test_input"))
            .iter()
            .map(|fish| fishes.multiply(*fish, 256))
            .sum::<u64>();
        assert_eq!(fish_count, 26984457539)
    }

    #[test]
    fn gold() {
        let mut fishes = Fishes::new();
        let fish_count = parse(include_str!("day_06_input"))
            .iter()
            .map(|fish| fishes.multiply(*fish, 256))
            .sum::<u64>();
        assert_eq!(fish_count, 1631647919273)
    }
}
