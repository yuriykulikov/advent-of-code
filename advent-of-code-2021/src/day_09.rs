use std::collections::HashSet;
use std::str::FromStr;

struct Cave {
    rows: Vec<Vec<u32>>,
}

#[derive(Clone, Debug, Hash, Eq, PartialEq)]
struct Point {
    x: u32,
    y: u32,
}

impl Cave {
    fn parse(input: &str) -> Self {
        let rows = input
            .lines()
            .map(|line| {
                line.split("")
                    .flat_map(|char| u32::from_str(char).ok())
                    .collect()
            })
            .collect();
        Cave { rows }
    }

    fn points(&self) -> Vec<Point> {
        let vec = self
            .rows
            .iter()
            .zip(0..self.rows.len())
            .flat_map(|(row, y)| {
                (0..row.len()).map(move |x| Point {
                    x: x as u32,
                    y: y as u32,
                })
            })
            .collect();
        vec
    }

    fn lookup(&self, point: &Point) -> Option<u32> {
        let row: Option<&Vec<u32>> = self.rows.get(point.y as usize);
        let value = row.map(|row| row.get(point.x as usize));
        value.flatten().map(|it| *it)
    }
}

impl Point {
    fn neighbours(&self) -> Vec<Point> {
        let mut neighbours = vec![
            Point {
                x: self.x + 1,
                y: self.y,
            },
            Point {
                x: self.x,
                y: self.y + 1,
            },
        ];
        if let Some(x) = self.x.checked_sub(1) {
            neighbours.push(Point { x, y: self.y });
        }
        if let Some(y) = self.y.checked_sub(1) {
            neighbours.push(Point { x: self.x, y });
        }
        neighbours
    }
}

fn low_points(cave: &Cave) -> Vec<Point> {
    cave.points()
        .into_iter()
        .filter(|point| {
            let value = cave.lookup(point).unwrap();
            point
                .neighbours()
                .iter()
                .flat_map(|neighbour| cave.lookup(neighbour))
                .all(|n_value| n_value > value)
        })
        .collect()
}

/// Flood fill the basins starting from low points
fn basins(cave: &Cave) -> Vec<HashSet<Point>> {
    low_points(cave)
        .into_iter()
        .map(|low_point| flood_fill(cave, low_point))
        .collect()
}

/// This can be optimized by only computing neighbors of the wave edge
fn flood_fill(cave: &Cave, low_point: Point) -> HashSet<Point> {
    let mut set: HashSet<Point> = HashSet::from([low_point]);
    loop {
        let frozen = set.clone();
        frozen
            .iter()
            .flat_map(|it| it.neighbours())
            .filter(|it| !frozen.contains(it))
            .filter(|it| cave.lookup(it).unwrap_or(9) != 9)
            .for_each(|point| {
                set.replace(point.clone());
            });
        if set.len() == frozen.len() {
            break;
        }
    }
    set
}

#[cfg(test)]
mod test {
    use itertools::Itertools;

    use crate::day_09::{basins, low_points, Cave};

    #[test]
    fn silver_test() {
        let cave = Cave::parse(include_str!("day_09_test_input"));
        assert_eq!(low_points(&cave).iter().count(), 4);

        assert_eq!(
            low_points(&cave)
                .iter()
                .map(|point| { cave.lookup(point).unwrap() + 1 })
                .sum::<u32>(),
            15
        );
    }

    #[test]
    fn silver() {
        let cave = Cave::parse(include_str!("day_09_input"));
        assert_eq!(
            low_points(&cave)
                .iter()
                .map(|point| { cave.lookup(point).unwrap() + 1 })
                .sum::<u32>(),
            465
        );
    }

    #[test]
    fn gold_test() {
        let cave = Cave::parse(include_str!("day_09_test_input"));
        assert_eq!(basins(&cave).len(), 4);
        let product = basins(&cave)
            .iter()
            .map(|it| it.len() as u32)
            .sorted()
            .rev()
            .take(3)
            .reduce(|l, r| l * r)
            .unwrap();
        assert_eq!(product, 1134);
    }

    #[test]
    fn gold() {
        let cave = Cave::parse(include_str!("day_09_input"));
        assert_eq!(basins(&cave).len(), 211);
        let product = basins(&cave)
            .iter()
            .map(|it| it.len() as u32)
            .sorted()
            .rev()
            .take(3)
            .reduce(|l, r| l * r)
            .unwrap();
        assert_eq!(product, 1269555);
    }
}
