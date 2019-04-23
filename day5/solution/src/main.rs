extern crate crypto;
extern crate rand;

use crypto::digest::Digest;
use crypto::md5::Md5;
use rand::Rng;

fn main() {
    solve();
}

fn solve() {
  let mut sh = Md5::new();
  let mut i = 1;
  let mut counter = 0;
  let mut part1 = vec![];
  let mut part2 = vec!['_', '_', '_', '_', '_', '_', '_', '_'];

  sh.input_str("abbhdwsy");

  while counter < 8 {
    sh.input_str(&format!("{}{}", "abbhdwsy", &i.to_string()));
    let hash = sh.result_str();
    let s = hash.as_bytes();

    if s.starts_with(b"00000") {
      if part1.len() < 8 {
        part1.push(s[5] as char);
      }

      let index = s[5] as usize - ('0' as usize);
      if index < 8 && part2[index] == '_' {
        part2[index] = s[6] as char;
        counter += 1;
      }
    }

    print!("\r");
    display_code(&part2);

    i += 1;
    sh.reset();
  }
}

fn display_code(code: &Vec<char>) {
  for i in code.iter() {
    if *i == '_' {
      print!("{}", rand::thread_rng().gen_range(0, 10));
    } else {
      print!("{}", i);
    }  
  }
}