# Readme

## 配置项

### ffmpeg

官网下载安装加到PATH即可

### Whisper

#### 创设虚拟环境

```bash
python -m venv venv
.\venv\Scripts\activate
```

安装whipser及其依赖(注意你所处的目录)

```bash
pip install git+https://github.com/openai/whisper.git
```

#### 运行和测试whisper

确保虚拟环境开启(或者将whisper加入PATH--推荐前者)

```bash
whisper audio/sample.wav --language Chinese --model base --output_dir output
```

更多参数和用法参阅文档

推荐选择的模型：

- small
- base
- medium

更推荐使用base，在速度和精度中做出取舍(事实上base的精度有点感人，相比之下medium好很多，但是速度慢)

注意：

运行whisper时会抛出类似警告

```bash
\venv\lib\site-packages\whisper\transcribe.py:132: UserWarning: FP16 is not supported on CPU; using FP32 instead
  warnings.warn("FP16 is not supported on CPU; using FP32 instead")
```

这是因为在CPU上运行不支持使用FP16（似乎是16位浮点精度吧） 会使用FP32替代

如果要使用FP16 需要在GPU上运行 这么做需要配置相应的PyTorch（目前不了解这个）

### JavaSpring

暂略

### 建议的目录架构

    ----dir  
      |— classnote-assistant  
      |---whisper-local  
          |-audio  
          |-output  
          |-venv
