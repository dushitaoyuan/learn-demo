package demo

import (
	"fmt"
	"time"
)

func say(s string) {
	for i := 0; i < 5; i++ {
		time.Sleep(100 * time.Millisecond)
		fmt.Println(i, s)
	}
}
func ss() {
	fmt.Println(111)
}
func main() {
	go say("world")
	say("hello")
	ss()
}
