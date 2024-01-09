FROM node:18-alpine
WORKDIR /ui

WORKDIR /ui
COPY . /ui

RUN npm install
EXPOSE 3000

ENTRYPOINT ["npm", "start"]
