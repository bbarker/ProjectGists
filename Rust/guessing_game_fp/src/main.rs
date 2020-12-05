#![feature(const_fn)]
#[macro_use]
extern crate tramp;

use rand::Rng;
use std::cmp::Ordering;
use std::io;
use tramp::{tramp, Rec};

fn main() {
    println!("Guess the number!");

    let secret_number = rand::thread_rng().gen_range(1, 101);

    tramp(main_loop(secret_number));

    fn main_loop(sec_num: u32) -> Rec<()> {
        let guess: u32 = tramp(get_valid_guess());
        println!("You guessed: {}", guess);

        match guess.cmp(&sec_num) {
            Ordering::Less => {
                println!("Too small!");
                rec_call!(main_loop(sec_num));
            }
            Ordering::Greater => {
                println!("Too big!");
                rec_call!(main_loop(sec_num));
            }
            Ordering::Equal => {
                rec_ret!(println!("You win!"));
            }
        }
    }

    fn get_valid_guess() -> Rec<u32> {
        println!("Please input your guess.");

        let mut guess = String::new();

        io::stdin()
            .read_line(&mut guess)
            .expect("Failed to read line");

        match guess.trim().parse() {
            Ok(num) => rec_ret!(num),
            Err(_) => rec_call!(get_valid_guess()),
        }
    }
}
