extern crate term;

use std::io::{BufReader};
use std::io::prelude::*;
use std::fs::File;
use std::thread;
use std::time::Duration;

const WIDTH: usize = 50;
const HEIGHT: usize = 6; 


fn main() {
    solve();
}

fn solve() {
  let mut lcd: Vec<u64> = vec![0; HEIGHT];
  let f = File::open("input.txt").expect("File not found");
  let mut t = term::stdout().unwrap();
  let f = BufReader::new(f);

  display_code(&lcd);

  for line in f.lines() {
    let ins = line.unwrap();
    let input: Vec<_> = ins.split(" ").collect();

    match input[0] {
      "rect" => {
        let dims: Vec<usize> = input[1].split("x")
          .map(|x| x.parse().unwrap()).collect();

        set_rectangle(&mut lcd, dims[0], dims[1]);
      }, 
      "rotate" => {
        let (dir, line) = input[2].split_at(2);
        let index = line.parse().unwrap();
        let shift = input[4].parse().unwrap();

        if dir.starts_with("x") {
          rotate_col(&mut lcd, index, shift);
        } else {
          rotate_row(&mut lcd, index, shift);
        }
      },
      _ => (),
    }

    for _ in 0..HEIGHT {
      t.cursor_up().unwrap();
    }

    display_code(&lcd);
    thread::sleep(Duration::from_millis(100));
  }

  t.reset().unwrap();

  let result: u32 = lcd.iter().map(|d| d.count_ones()).sum();
  println!("Pixels lit: {}", result);
}

fn set_rectangle(lcd: &mut Vec<u64>, x: usize, y: usize) {
  let mut z = 0;
  while z < y {
    lcd[z] |= (1u64 << x) - 1;
    z += 1;
  }
}

fn rotate_row(lcd: &mut Vec<u64>, x: usize, y: usize) {
  let res = (lcd[x] << y) | (lcd[x] >> (WIDTH - y));
  lcd[x] = res & ((1u64 << WIDTH) - 1);
}

fn rotate_col(lcd: &mut Vec<u64>, x: usize, y: usize) {
  let mut temp :u64 = 0;
  for i in 0..HEIGHT {
    temp |= ((lcd[i] >> x) & 1) << i;
    lcd[i] ^= lcd[i] & (1u64 << x);
  }

  temp = (temp << y) | (temp >> (HEIGHT - y));

  for i in 0..HEIGHT {
    lcd[i] |= ((temp >> i) & 1) << x;
  }
}

fn display_code(lcd: &Vec<u64>) {
  for i in 0..HEIGHT {
    for j in 0..WIDTH {
      if ((lcd[i] >> j) & 1) == 0 {
        print!(".");
      } else {
        print!("#");
      }
    }
    print!("\n");
  }
}

