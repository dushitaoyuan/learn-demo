package demo

import (
	"fmt"
	"github.com/jinzhu/gorm"

	//"demo/src/demo"
	_ "github.com/go-sql-driver/mysql"
	_ "github.com/jinzhu/gorm/dialects/mysql"
	"time"
)

type User struct {
	Id       int `gorm:"primary_key"`
	Age      int
	Name     string `gorm:"size:255"`
	Birthday time.Time
}

func Orm() {
	db, err := gorm.Open("mysql", "root:root@tcp(localhost:3306)/demo?charset=utf8&parseTime=True&loc=Local")
	db.LogMode(true)
	if err != nil {
		panic(err)
	}
	if !db.HasTable("users") {
		db.Table("users").Set("gorm:table_options", "ENGINE=InnoDB").CreateTable(&User{})
	}
	var u User
	if db.Where("id=?", 1).First(&u) != nil {
		fmt.Println(fmt.Sprintf("id:%d,age:%d,name:%s,birthday:%s", u.Id, u.Age, u.Name, u.Birthday))
	}
	//执行原生sql
	var u2 User
	db.Raw("SELECT * FROM users WHERE id=?", 1).Scan(&u2)
	fmt.Println(fmt.Sprintf("id:%d,age:%d,name:%s,birthday:%s", u2.Id, u2.Age, u2.Name, u2.Birthday))
	db.Close()

}
