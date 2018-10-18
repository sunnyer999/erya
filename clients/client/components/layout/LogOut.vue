<template>
  <div>
    <card-modal :visible="visible" okText="Log Out" @ok="ok" @cancel="cancel" @close="close" :title="title" transition="zoom">
    <div class="modal-content" style="width:300px;">
      <div class="box">
            <h3 class="title is-4">Do you want to log out?</h3>
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
        this.user.username = ''
        this.user.password = ''
        this.$store.dispatch('getusername', this.user)
      },
      cancel () {
        this.$emit('cancel')
      }
    }
  }

</script>
