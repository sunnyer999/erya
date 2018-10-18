<template>
  <div>
    <card-modal :visible="visible" okText="Submit" @ok="ok" @cancel="cancel" @close="close" :title="title" transition="zoom">
    <div class="modal-content" style="width:300px;">
      <div class="box">
            <h3 class="title is-4">System Login</h3>
            <p class="control has-icon">
            <input class="input" type="text" placeholder="Username" v-model="user.username" >
            <span class="icon is-small">
              <i class="fa fa-user"></i>            
            </span>
            </p>
            <p class="control has-icon">
            <input class="input" type="password" placeholder="Password" v-model="user.password">
            <span class="icon is-small">
              <i class="fa fa-lock"></i>
            </span>  
            </p>
       </div>     
    </div>  
    </card-modal>
  </div>
</template>


<script>

  import { CardModal } from 'vue-bulma-modal'
 // import _ from 'lodash'

  export default {
    props: {
      visible: Boolean,
      title: String,
      user: {
        type: Object,
        default: () => {
          return {
            username: '',
            password: ''
          }
        }
      }
    },
    data () {
      return {
        user: this.user
      }
    },
    components: {
      CardModal
    },

    computed: {
    },

    methods: {
      open (url) {
        window.open(url)
      },
      close () {
        this.$emit('close')
      },
      ok () {
      //  console.log({'user': this.user})
        this.$emit('ok')
        this.$store.dispatch('getusername', this.user)
      },
      cancel () {
        this.$emit('cancel')
      }
    }
  }

</script>
