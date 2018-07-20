# -*- coding: utf-8 -*-
import scrapy


class QuotesSpider(scrapy.Spider):
    name = "quotes"
    # allowed_domains = ["http://quotes.toscrape.com/page/1/"]
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
