#-*- coding:utf-8 -*-
import scrapy
import re
from doubanbook.items import DoubanbookItem

class DbbookSpider(scrapy.Spider):
    name = "dbbook"
    # allowed_domains = ["https://www.douban.com/doulist/1264675/"]
    start_urls = ['https://www.douban.com/doulist/1264675//']

    def parse(self, response):
        item = DoubanbookItem()
        selector = scrapy.Selector(response)
        books = selector.xpath('//div[@class="bd doulist-subject"]')
        for each in books:
            # print each.extract()
            t = each.xpath('div[@class="title"]/a/text()').extract();
            title = t[0].replace(' ','').replace('\n','')
            r = each.xpath('div[@class="rating"]/span[@class="rating_nums"]/text()').extract()
            rate = r[0] if len(r) > 0 else ""
            author = re.search('<div class="abstract">(.*?)<br',each.extract(),re.S).group(1)
            author = author.replace(' ','').replace('\n','')
            print 'Title:' + title
            print 'Rate:' + rate
            print author
            print ''
            item['title'] = title
            item['rate'] = rate
            item['author'] = author
            yield item
            nextPage = selector.xpath('//span[@class="next"]/link/@href').extract()
            if nextPage:
                next = nextPage[0]
                # print next
                yield scrapy.http.Request(next, callback=self.parse)
