package demo

import "net/http"
import "github.com/gin-gonic/gin"

func Start_http() {
	router := gin.Default()
	router.StaticFS("/public", http.Dir("G://简历"))
	router.GET("/ping", func(c *gin.Context) {
		c.JSON(200, gin.H{
			"message": "pong",
		})
	})
	router.Run(":8080")
}
