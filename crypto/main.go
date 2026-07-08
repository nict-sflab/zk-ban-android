package zkbancrypto

import (
	_ "embed"
	"time"

	client "github.com/akakou/zk-ban-system/client/signer"
	"github.com/akakou/zk-ban-system/utils"
	"github.com/akakou/zk-ban/dump"
	"github.com/akakou/zk-ban/load"
	_ "golang.org/x/mobile/bind"
)

func SetPath(path string) {
	dump.KeyPath = path
}

func SetPeriodUnit(unit int64) {
	utils.PeriodUnit = time.Duration(unit)
}

func GetPeriod() int64 {
	return utils.Period()
}

func ImportKeys() {
	load.ReDumpUserKey("", "join")
	load.ReDumpUserKey("", "sign")
	load.ReDumpUserKey("sample", "update")
}

func RequestJoin(idToken, url string) ([]byte, error) {
	return client.RequestJoin(idToken, url)
}

func Sign(message []byte, count int64, signer, gpk []byte, url string) ([]byte, error) {
	return client.Sign(message, count, signer, gpk, url)
}

func RequestUpdate(signer, rl, gpk []byte, url string) ([]byte, error) {
	return client.RequestUpdate(signer, rl, gpk, url)
}

func FetchGroupPublicKey(url string) ([]byte, error) {
	return client.FetchGroupPublicKey(url)
}

func FetchRevocationList(signer []byte, url string) ([]byte, error) {
	return client.FetchRevocationList(signer, url)
}
