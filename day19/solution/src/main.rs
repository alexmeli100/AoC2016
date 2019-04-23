use std::f32;
use std::collections::VecDeque;

fn main() {
  println!("Part1: {}\nPart2: {}", part1(3017957), part2(3017957));
}

fn part1(x :u32) -> u32 {
  let result = 2 * (x - 2u32.pow(f32::log2(x as f32) as u32)) + 1;
  result
}

fn part2(x :u32) -> u32 {
  let mut q1: VecDeque<u32> = (1..(x+1)/2 +1 ).collect();
  let mut q2: VecDeque<u32> = ((x+1)/2 + 1..(x+1)).collect();

  while q1.len() + q2.len() != 1 {
    let y = q1.pop_front().unwrap() ;

    if q1.len() == q2.len() {
      q1.pop_back();
    } else {
      q2.pop_front();
    }

    q2.push_back(y);
    q1.push_back(q2.pop_front().unwrap());
  }

  q1.pop_front().unwrap()
}

