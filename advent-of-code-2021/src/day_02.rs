use std::str::FromStr;

pub struct Submarine {
    x: i32,
    y: i32,
    aim: i32,
}

impl Submarine {
    pub fn new() -> Self {
        Submarine { x: 0, y: 0, aim: 0 }
    }

    pub fn step_silver(&self, dx: i32, dy: i32) -> Self {
        Submarine {
            x: self.x + dx,
            y: self.y + dy,
            aim: self.aim,
        }
    }

    pub fn step_gold(&self, dx: i32, dy: i32) -> Self {
        let aim = self.aim + dy;
        Submarine {
            x: self.x + dx,
            y: self.y + aim * dx,
            aim,
        }
    }
}

fn parse_steps(input: &str) -> Vec<(i32, i32)> {
    input.lines()
        .map(|line| {
            if let Some((direction, d_str)) = line.split_once(" ") {
                let d = i32::from_str(d_str).unwrap();
                match direction {
                    "forward" => (d, 0),
                    "up" => (0, -d),
                    "down" => (0, d),
                    _ => panic!("unsupported!"),
                }
            } else {
                (0, 0)
            }
        })
        .collect()
}

#[cfg(test)]
mod test {
    use crate::day_02::{parse_steps, Submarine};

    #[test]
    fn silver_test() {
        let submarine = parse_steps(include_str!("day_02_test_input"))
            .into_iter()
            .fold(Submarine::new(), |acc, (x, y)| acc.step_silver(x, y));
        assert_eq!(submarine.x * submarine.y, 150);
    }

    #[test]
    fn silver() {
        let submarine = parse_steps(include_str!("day_02_input"))
            .into_iter()
            .fold(Submarine::new(), |acc, (x, y)| acc.step_silver(x, y));
        assert_eq!(submarine.x * submarine.y, 2073315);
    }

    #[test]
    fn gold_test() {
        let submarine = parse_steps(include_str!("day_02_test_input"))
            .into_iter()
            .fold(Submarine::new(), |acc, (x, y)| acc.step_gold(x, y));
        assert_eq!(submarine.x * submarine.y, 900);
    }

    #[test]
    fn gold() {
        let submarine = parse_steps(include_str!("day_02_input"))
            .into_iter()
            .fold(Submarine::new(), |acc, (x, y)| acc.step_gold(x, y));
        assert_eq!(submarine.x * submarine.y, 1840311528);
    }
}