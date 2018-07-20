# 学校信息爬虫
## 来源网站
http://xuexiao.51sxue.com/slist/?t=2&areaCodeS=12&level=3&page=3

## URL参数说明

| 参数| 说明|
| ----| ----|
| t  | 学校类型|
| areaCodeS | 城市编号|
| level | 学校属性 |
| page | 页码 |

## 配置
保存csv文件地址

settings.py >
FEED_URI = {PATH}


## 使用方式

替换school.py > start_urls

执行

`>>scrapy crawl school`
