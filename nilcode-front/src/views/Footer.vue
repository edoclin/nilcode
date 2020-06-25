<template>
    <div>
        <br>
        <el-row>
         <el-col :xs="{span: 24}" :sm="{span: 24}">        <img :src="QRCode" style="width: 8%"/>
         </el-col>
        </el-row>
        <br>
        <el-row :gutter="0" style="height: inherit">
            <el-col :xs="{span: 24}" :sm="{span: 8}">
                <el-link target="_blank" type="danger" href="https://www.github.com/eZeroa">版权保留 © 2020</el-link>

            </el-col>
            <el-col :xs="{span: 24}" :sm="{span: 8}" style="height: 60%">
                <el-link type="primary" style="float: bottom" href="mailto:nilcode@nilcode.cn">联系作者</el-link>
            </el-col>
            <el-col :xs="{span: 24}" :sm="{span: 8}">
                <el-link target="_blank" type="warning" href="http://www.beian.miit.gov.cn/" >蜀ICP备19024631号-2</el-link>
            </el-col>
        </el-row>
    </div>
</template>

<script>
    export default {
        name: "Footer",
        data() {
            return {
                QRCode: require('@/assets/QRCode.png'),
                singleLogo: require('@/assets/singleLogo.png')
            }
        }
    }
</script>

<style scoped>

</style>
