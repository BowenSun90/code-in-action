# Scrapy爬虫入门

## Scrapy安装
Scrapy在Python 2.7和Python 3.3或更高版本上运行   
通用方式：可以从pip安装Scrapy及其依赖：
```
> pip install Scrapy
```

### 创建项目
```
> scrapy startproject tutorial
```
项目结构：
```
+- tutorial/
|  +- scrapy.cfg           # 部署配置文件
|  +- tutorial/            # Python模块,代码写在这个目录下
|  |  +- __init__.py
|  |  +- items.py          # 项目项定义文件
|  |  +- pipelines.py      # 项目管道文件
|  |  +- settings.py       # 项目设置文件
|  |  +- spiders/          # 我们的爬虫/蜘蛛 目录
|  |  |  +- __init__.py
```

#### 创建第一个爬虫类
`tutorial/spiders/quotes.py`
```
import scrapy

class QuotesSpider(scrapy.Spider):
    name = "quotes"

    start_urls = [
       'http://quotes.toscrape.com/page/1/',
       'http://quotes.toscrape.com/page/2/',
   ]

    def start_requests(self):
        for url in self.start_urls:
            yield scrapy.Request(url=url, callback=self.parse)

    def parse(self, response):
        page = response.url.split("/")[-2]
        filename = 'quotes-%s.html' % page
        with open(filename, 'wb') as f:
            f.write(response.body)
        self.log('Saved file %s' % filename)

```
- 必须继承 scrapy.Spider

- name：标识爬虫。它在项目中必须是唯一的，也就是说，您不能为不同的Spider设置相同的名称。

- start_requests()：必须返回一个迭代的Requests（你可以返回请求列表或写一个生成器函数），Spider将开始抓取。后续请求将从这些初始请求连续生成。

- parse()：将被调用来处理为每个请求下载的响应的方法。 response参数是一个TextResponse保存页面内容的实例，并且具有更多有用的方法来处理它。

  该parse()方法通常解析响应，提取抓取的数据作为词典，并且还找到要跟踪的新网址并从中创建新的请求（Request）。

#### 运行爬虫
```
> cd tutorial
> scrapy crawl quotes  # quotes是上文写的爬虫名称
```
```
2018-07-20 10:27:21 [scrapy.statscollectors] INFO: Dumping Scrapy stats:
{'downloader/request_bytes': 675,
 'downloader/request_count': 3,
 'downloader/request_method_count/GET': 3,
 'downloader/response_bytes': 5976,
 'downloader/response_count': 3,
 'downloader/response_status_count/200': 2,
 'downloader/response_status_count/404': 1,
 'finish_reason': 'finished',
 'finish_time': datetime.datetime(2018, 7, 20, 2, 27, 21, 704027),
 'log_count/DEBUG': 6,
 'log_count/INFO': 7,
 'response_received_count': 3,
 'scheduler/dequeued': 2,
 'scheduler/dequeued/memory': 2,
 'scheduler/enqueued': 2,
 'scheduler/enqueued/memory': 2,
 'start_time': datetime.datetime(2018, 7, 20, 2, 27, 14, 306348)}
2018-07-20 10:27:21 [scrapy.core.engine] INFO: Spider closed (finished)
```
现在，当前目录中的文件，已经创建了两个新文件：`quotes-1.html`和`quotes-2.html`，以及相应URL的内容，parse方法解析的内容。

### 提取数据
#### TODO
