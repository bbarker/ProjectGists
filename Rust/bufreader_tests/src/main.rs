use std::env;
use std::fs::File;
use std::io::prelude::*;
use std::io::BufReader;

fn main() -> std::io::Result<()> {
    let argc = env::args().count();

    if argc != 3 {
        panic!("cp: missing source, destination, or both");
    }

    let f = File::open("log.txt")?;
    let mut reader = BufReader::new(f);

    let mut line = String::new();
    let len = reader.read_line(&mut line)?;
    println!("First line is {} bytes long", len);
    Ok(())
}
