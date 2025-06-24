now=$(date +"%Y_%m_%d_%H")
# need docker, ossutil

#BUCKET=
#MYSQL_USER=
#MYSQL_PASSWORD=
#MYSQL_HOST=
#DB=
# db_path need to change
db_path="/server/data/backup/${DB}_${now}.sql.gz"

docker run --rm --network=host mysql:8.4 mysqldump \
  --opt -h ${MYSQL_HOST} -u ${MYSQL_USER} -p${MYSQL_PASSWORD} --add-drop-database --complete-insert ${DB} | gzip > ${db_path}

# secret the db_path, using FileCrypto

# oss path need to change
/server/bin/backup/ossutil -c /server/conf/backup/.ossutilconfig cp ${db_path} oss://${BUCKET}/office/db/${DB}/