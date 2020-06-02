# Chinese-WestLaw-Web-Search-Mining
A simple Chinese WestLaw Web searching system, as the final project for WSM2020, SJTU.

## Dataset
The dataset is provided by [Kenny Zhu](http://cs.sjtu.edu.cn/~kzhu/), 
with court records of legal cases in China in Chinese.
You can download raw data and feed it into our system as follows.

```
cd src/main/resources
wget https://adapt.seiee.sjtu.edu.cn/wsm2020/wsm_proj1_data/data1.zip
wget https://adapt.seiee.sjtu.edu.cn/wsm2020/wsm_proj1_data/data2.zip
wget https://adapt.seiee.sjtu.edu.cn/wsm2020/wsm_proj1_data/instruments.zip
unzip data1.zip && unzip data2.zip && unzip instruments.zip
```

## Requirements
Our system has the following requirements.
- Java, version 11+
- Gradle, version 5.0.0+

## Contributors
- [Jilai Zheng](https://github.com/zhengjilai)
- [Zhiming Fan](https://github.com/Lyears)