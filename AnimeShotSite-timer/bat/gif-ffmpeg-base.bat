
set "videoPath=%1"
set "outGifPath=%2"
set "subtitlePath1=%3"
set "subtitlePath2=%4"
set "startTime=%5"
set "duationTime=%6"
set "bayerLevel=%7"

set "palette=G:\AnimeShotSite\anime\J\����սʿ�ߴ�\��Ѫ�Ĺ¶�\�ڶ���\palette.png"
set "filters=fps=6, scale=480:-1:flags=lanczos, "

ffmpeg -v warning -ss %startTime% -t %duationTime% -i %videoPath%                      -vf "%filters% palettegen" -y %palette%
ffmpeg -v warning -ss %startTime% -t %duationTime% -i %videoPath% -i %palette% -lavfi "%filters% paletteuse=dither=bayer:bayer_scale=%bayerLevel%" -y %outGifPath%


pause