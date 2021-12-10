use std::str::FromStr;

#[derive(Debug, Copy, Clone)]
struct Line {
    start: Point,
    end: Point,
}

impl Line {
    /// Returns line points in no particular order
    fn points(&self) -> Vec<Point> {
        if self.is_vertical() {
            let x = self.start.x;
            points(self.start.y, self.end.y)
                .into_iter()
                .map(|y| Point { x, y })
                .collect::<Vec<Point>>()
        } else if self.is_horizontal() {
            let y = self.start.y;
            points(self.start.x, self.end.x)
                .into_iter()
                .map(|x| Point { x, y })
                .collect::<Vec<Point>>()
        } else {
            let x_points = points(self.start.x, self.end.x);
            let y_points = points(self.start.y, self.end.y);
            assert_eq!(x_points.len(), y_points.len());
            x_points
                .into_iter()
                .zip(y_points)
                .map(|(x, y)| Point { x, y })
                .collect()
        }
    }

    fn is_vertical(&self) -> bool {
        self.start.x == self.end.x
    }

    fn is_horizontal(&self) -> bool {
        self.start.y == self.end.y
    }
}

/// Returns a vector of 1-dimensional points between from and to. If to < from,
/// vec contains points in descending order.
fn points(from: u32, to: u32) -> Vec<u32> {
    if from < to {
        (from..=to).collect()
    } else {
        (to..=from).rev().collect()
    }
}

#[derive(Debug, Copy, Clone, Eq, PartialEq, Hash)]
struct Point {
    x: u32,
    y: u32,
}

impl Point {
    fn parse(str: &str) -> Point {
        let (x, y) = str.split_once(",").unwrap();
        Point {
            x: u32::from_str(x.trim()).unwrap(),
            y: u32::from_str(y.trim()).unwrap(),
        }
    }
}

/// 5,5 -> 8,2
fn parse(input: &str) -> Vec<Line> {
    input
        .lines()
        .filter_map(|line| line.split_once("->"))
        .map(|(start, end)| Line {
            start: Point::parse(start),
            end: Point::parse(end),
        })
        .collect()
}

#[cfg(test)]
mod test {
    use itertools::Itertools;

    use crate::day_05::{parse, Point};

    #[test]
    fn silver_test() {
        let points = parse(include_str!("day_05_test_input"))
            .iter()
            .filter(|line| line.is_vertical() || line.is_horizontal())
            .flat_map(|line| line.points())
            .collect::<Vec<Point>>();

        let overlaps_of_2 = points
            .iter()
            .counts()
            .values()
            .filter(|&&count| count >= 2)
            .count();

        assert_eq!(overlaps_of_2, 5);
    }

    #[test]
    fn silver() {
        let points = parse(include_str!("day_05_input"))
            .iter()
            .filter(|line| line.is_vertical() || line.is_horizontal())
            .flat_map(|line| line.points())
            .collect::<Vec<Point>>();

        let overlaps_of_2 = points
            .iter()
            .counts()
            .values()
            .filter(|&&count| count >= 2)
            .count();

        assert_eq!(overlaps_of_2, 5167);
    }

    #[test]
    fn gold_test() {
        let points = parse(include_str!("day_05_test_input"))
            .iter()
            .flat_map(|line| line.points())
            .collect::<Vec<Point>>();

        let overlaps_of_2 = points
            .iter()
            .counts()
            .values()
            .filter(|&&count| count >= 2)
            .count();

        assert_eq!(overlaps_of_2, 12);
    }

    #[test]
    fn gold() {
        let points = parse(include_str!("day_05_input"))
            .iter()
            .flat_map(|line| line.points())
            .collect::<Vec<Point>>();

        let overlaps_of_2 = points
            .iter()
            .counts()
            .values()
            .filter(|&&count| count >= 2)
            .count();

        assert_eq!(overlaps_of_2, 17604);
    }
}
