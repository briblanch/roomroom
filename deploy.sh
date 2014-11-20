pushd .

#build the webapp and stage the assets
cd webapp
grunt build

popd

pushd .

# deploy the app to appengine
cd backend/src
appcfg.py update .

popd .