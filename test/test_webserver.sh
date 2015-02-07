i="0"

while [ $i -lt 50 ]
do
wget localhost:3000
wget --post-data="" localhost:3000
rm index.html
rm index.html
done