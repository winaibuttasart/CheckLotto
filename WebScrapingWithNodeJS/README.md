### Web Scraping

Clone Project ที่ (Github)[https://github.com/winaibuttasart/CheckLotto.git]
แก้ตัวแปร config เป็น Key firebase โปรเจคของตัวเอง จากนั้นใช้คำสั่ง
```sh
$ npm install
```

กรณีต้องการเพิ่มข้อมูลลอตเตอรี่ใหม่ทั้งหมด ใช้คำสั่ง
```sh
$ node scrapAllDay.js
```

หรือต้องการเพิ่มข้อมูลเพียงวันเดียว ให้ใช้คำสั่ง
```sh
$ node scrapOneDay.js
```

<br>หลังจากใช้คำสั่ง<br>
ตรวจสอบข้อมูลว่าเพิ่มเข้าในฐานข้อมูลหรือยัง ที่โปรเจค Firebase ของตัวเอง