<template>
  <div class="columns">
    <div class="column is-8">
      <article class="tile is-child box">
        <h1 class="title">Issues List</h1>
        <div class="block">
          <el-collapse accordion>
            <el-collapse-item title="Feedback" name="2">
              <div>控制反馈：通过界面样式和交互动效让用户可以清晰的感知自己的操作；</div>
              <div>页面反馈：操作后，通过页面元素的变化清晰地展现当前状态。</div>
            </el-collapse-item>
            <el-collapse-item title="Efficiency" name="3">
              <div>简化流程：设计简洁直观的操作流程；</div>
              <div>清晰明确：语言表达清晰且表意明确，让用户快速理解进而作出决策；</div>
              <div>帮助用户识别：界面简单直白，让用户快速识别而非回忆，减少用户记忆负担。</div>
            </el-collapse-item>
            <el-collapse-item title="Controllability" name="4">
              <div>用户决策：根据场景可给予用户操作建议或安全提示，但不能代替用户进行决策；</div>
              <div>结果可控：用户可以自由的进行操作，包括撤销、回退和终止当前操作等。</div>
            </el-collapse-item>
          </el-collapse>
        </div>
        <div class="block">
          <el-pagination small layout="prev, pager, next" :total="1000">
          </el-pagination>
        </div>
      </article>
    </div>

    <div class="column is-4">
      <article class="tile is-child box">
        <h1 class="title">Please Tell Us</h1>

        <el-form ref="form" :model="form" label-width="80px">
          <el-form-item label="Title">
            <el-input v-model="form.title" placeholder="Write a title"></el-input>
          </el-form-item>

          <el-form-item label="Comment">
            <el-input type="textarea" v-model="form.comment" placeholder="Leave a comment"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submit">Submit</el-button>
          </el-form-item>
        </el-form>

      </article>
    </div>
  </div>

  </div>
  </div>
</template>

<script>
  export default {
    data () {
      return {
        form: {
          title: '',
          comment: '',
          value: []
        },
        options: [{
          value: 'question',
          label: 'question'
        }, {
          value: 'help',
          label: 'help wanted'
        }, {
          value: 'enhancement',
          label: 'enhancement'
        }, {
          value: 'other',
          label: 'other'
        }]
      }
    },

    methods: {
      postIssueData (action) {
        if (action === 'confirm') {
          this.$store.dispatch('postIssueData', { 'vm': this })
        }
      },

      submit () {
        console.log(this.form)
        this.$confirm('This will submit the issue. Continue?', 'Confirm', {
          confirmButtonText: 'yes',
          cancelButtonText: 'cancel',
          callback: this.postIssueData,
          type: 'info'
        })
      }
    }
  }

</script>
