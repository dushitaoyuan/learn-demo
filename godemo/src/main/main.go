package main

import (
	"fmt"
	"github.com/jinzhu/gorm"

	//"demo/src/demo"
	_ "github.com/go-sql-driver/mysql"
	_ "github.com/jinzhu/gorm/dialects/mysql"
	"time"
)

type User struct {
	birthday time.Time
	age      int
	name     string `gorm:"size:255"`
	id       int    `gorm:"primary_key"`
}

func main() {
	db, err := gorm.Open("mysql", "root:root@(localhost:3306)/demo?charset=utf8&parseTime=True&loc=Local")
	if err != nil {
		panic(err)
	}
	var result User
	db.Raw("SELECT id,name,age,birthday from usres where id =?", 1).Scan(&result)
	fmt.Println(result.id)
	fmt.Println(result.name)
}
