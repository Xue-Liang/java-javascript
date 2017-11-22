#/bin/sh
for i in `ls /home/xue/rs` 
do  
	total=`ls /home/xue/rs/$i|wc -l`
        if [ $total -eq 0 ] 
        then 
          rm -fr "/home/xue/rs/"$i 
        fi
        echo $total
        #echo $i
done
