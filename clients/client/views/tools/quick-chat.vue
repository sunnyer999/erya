<template>
  <div>
    <div class="tile is-ancestor">

      <div class="tile is-parent">
        <div class="tile is-child box">
          <el-card>
            <div style="padding: 14px;">
              <div class="bottom clearfix">
                <time class="time"></time>
                <img src="~assets/avatar/avatar1.png" alt="Image">
                <img src="~assets/avatar/avatar2.png" alt="Image">
                <img src="~assets/avatar/avatar3.png" alt="Image">
                <img src="~assets/avatar/avatar4.png" alt="Image">
                <img src="~assets/avatar/avatar5.png" alt="Image">
                <img src="~assets/avatar/avatar6.png" alt="Image">
                <img src="~assets/avatar/avatar7.png" alt="Image">
              </div>
            </div>
          </el-card>
        </div>
      </div>
    </div>

    <div class="tile is-ancestor">
      <div class="tile is-parent is-8">
        <div class="tile is-child box">

          <div class="card">
            <header class="card-header">
              <a class="card-header-icon">
                <span class="icon" id="quick-chat">
                <i class="fa fa-comments" aria-hidden="true"></i></span>
                </span>
              </a>
              <p class="card-header-title">
                Quick Chat {{ foo }}
              </p>
            </header>


            <div class="card-content" id="message-panel">
              <div class="content" id="content">
                <div class="columns" v-for="message in messages">
                  <div v-bind:class="[message.id == id ? left: right]">
                    <article class="media">
                      <div class="media-left" v-if="message.id != id">
                        <figure class="image is-64x64">
                          <img src="~assets/avatar/avatar2.png" alt="Image">
                        </figure>
                      </div>
                      <div class="media-content ">
                        <div v-bind:class="[message.id == id ? warn : primary]">
                          <p>
                            <strong>John Smith</strong> <small>@johnsmith</small> <small>31m</small>
                            <br> {{message.content}}
                          </p>
                        </div>
                      </div>

                      <div class="media-right" v-if="message.id == id">
                        <figure class="image is-64x64">
                          <img src="~assets/avatar/avatar3.png" alt="Image">
                        </figure>
                      </div>
                    </article>
                  </div>
                </div>

              </div>
            </div>
            <footer class="card-footer">
              <a class="card-footer-item">
                <input class="input" type="text" placeholder="Text input" v-model="text" @keyup.enter="send(text)">
                <span class="icon" id="emoji">
                <emoji-icon class="e1a-grinning" ></emoji-icon>
              </span>
                <span class="icon" id="attachment">
                <i class="fa fa-paperclip" aria-hidden="true"></i>
              </span>
              </a>
            </footer>
          </div>

        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import { EmojiIcon } from 'vue-bulma-emoji'
  var websocket = new WebSocket('ws://10.220.132.186:8084/websocket') // eslint-disable-line no-undef
  
  window.addEventListener('load', function () {
    if (Notification && Notification.permission !== 'granted') {  // eslint-disable-line no-undef
      Notification.requestPermission(function (status) {    // eslint-disable-line no-undef
        if (Notification.permission !== status) {           // eslint-disable-line no-undef
          Notification.permission = status                  // eslint-disable-line no-undef
        }
      })
    }
  })

  export default {
    data () {
      return {
        messages: [],
        text: '',
        id: parseInt(Math.random() * 1000 + 1000),
        primary: 'notification is-primary',
        warn: 'notification is-warning',
        right: 'column is-half',
        left: 'column is-half is-offset-6',
        foo: 'hello world',
        options: {
          dir: 'ltr',
          lang: 'utf-8',
          icon: 'logo.png',
          body: 'hello veritas'
        }
      }
    },
    methods: {
      send (text) {
        if (text) {
          websocket.send(this.id + ':' + text)
        }
        this.text = ''
        this.scrollToEnd()
      },
      scrollToEnd () {
        var messagePanel = document.getElementById('content')
        messagePanel.scrollTop = messagePanel.scrollHeight
      }
    },
    components: {
      EmojiIcon
    },
    created () {
      var vm = this
      websocket.onopen = function (event) {
        console.log('connected', event)
      }
      websocket.onmessage = function (event) {
        var foo = event.data.split(':')
        vm.messages.push({ id: foo[0], content: foo[1] })
        if (parseInt(foo[0]) !== vm.id) {
          vm.options.body = foo[1]
          var n = new Notification(vm.id, vm.options) // eslint-disable-line no-undef
          console.log(n)
        }
      }
    },
    read () {
      console.log('readying')
    }
  }

</script>

<style>
  #quick-chat {
    color: #8492A6
  }

  #emoji {
    margin-left: 5px
  }

  #attachment {
    margin-top: 3px;
    margin-left: 2px
  }

  .content {
    height: 500px;
    overflow-y: auto;
    overflow-x: hidden
  }
</style>
