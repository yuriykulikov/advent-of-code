use itertools::Itertools;

fn corrupted(chunk: &String) -> Option<char> {
    let mut stack = vec![];
    for next in chunk.chars() {
        match next {
            '(' | '[' | '{' | '<' => stack.push(next),
            ')' | ']' | '}' | '>' => match stack.pop() {
                Some(prev) => {
                    if invert(next) == prev {
                        continue;
                    } else {
                        return Some(next);
                    }
                }
                None => return None,
            },
            _ => panic!("Unknown {}", next),
        }
    }
    None
}

fn incomplete(chunk: &String) -> Vec<char> {
    let mut stack = vec![];
    for next in chunk.chars() {
        match next {
            '(' | '[' | '{' | '<' => stack.push(next),
            ')' | ']' | '}' | '>' => match stack.pop() {
                Some(prev) => {
                    if invert(next) == prev {
                        continue;
                    } else {
                        panic!("Corrupted!")
                    }
                }
                None => break,
            },
            _ => panic!("Unknown {}", next),
        }
    }
    stack.iter().map(|e| invert(*e)).rev().collect()
}

fn invert(char: char) -> char {
    match char {
        ')' => '(',
        ']' => '[',
        '}' => '{',
        '>' => '<',
        '(' => ')',
        '[' => ']',
        '{' => '}',
        '<' => '>',
        _ => panic!(),
    }
}

fn corrupted_scores(chunks: Vec<String>) -> u32 {
    chunks
        .iter()
        .filter_map(|chunk| corrupted(chunk))
        .map(|corr| match corr {
            ')' => 3,
            ']' => 57,
            '}' => 1197,
            '>' => 25137,
            _ => 0,
        })
        .sum::<u32>()
}

fn autocomplete_scores(chunks: Vec<String>) -> Vec<u64> {
    chunks
        .into_iter()
        .filter(|it| corrupted(it) == None)
        .map(|it| incomplete(&it))
        .map(|it| {
            it.iter().fold(0_u64, |acc, next| {
                acc * 5
                    + match next {
                        ')' => 1,
                        ']' => 2,
                        '}' => 3,
                        '>' => 4,
                        _ => panic!(),
                    }
            })
        })
        .sorted()
        .collect()
}

#[cfg(test)]
mod test {
    use crate::day_10::{autocomplete_scores, corrupted_scores};

    #[test]
    fn silver_test() {
        let chunks: Vec<String> = include_str!("day_10_test_input")
            .lines()
            .map(|line| line.to_owned())
            .collect();
        assert_eq!(corrupted_scores(chunks), 26397);
    }

    #[test]
    fn silver() {
        let chunks: Vec<String> = include_str!("day_10_input")
            .lines()
            .map(|line| line.to_owned())
            .collect();
        assert_eq!(corrupted_scores(chunks), 240123);
    }

    #[test]
    fn gold_test() {
        let chunks: Vec<String> = include_str!("day_10_test_input")
            .lines()
            .map(|line| line.to_owned())
            .collect();
        let autocomplete_scores: Vec<u64> = autocomplete_scores(chunks);
        assert_eq!(
            *autocomplete_scores
                .get(autocomplete_scores.len() / 2)
                .unwrap(),
            288957
        );
    }

    #[test]
    fn gold() {
        let chunks: Vec<String> = include_str!("day_10_input")
            .lines()
            .map(|line| line.to_owned())
            .collect();
        let autocomplete_scores: Vec<u64> = autocomplete_scores(chunks);
        assert_eq!(
            *autocomplete_scores
                .get(autocomplete_scores.len() / 2)
                .unwrap(),
            3260812321
        );
    }
}
