import type React from "react"

const logos = [
  { name: "gradCapital", src: "/images/grad.png" },
  { name: "Naukri", src: "/images/naukri.png" },
  { name: "IIITD", src: "/images/iiitd.png" },
  { name: "VIT", src: "/images/vit.png" },
  { name: "SNU", src: "/images/snu.png" },
]

export const BackedBySection: React.FC = () => {
  return (
    <section className="bg-zinc-950/50 py-16 border-t border-white/5">
      <div className="max-w-7xl mx-auto px-4">
        <h2 className="text-center text-sm font-semibold tracking-wider text-gray-500 uppercase mb-10">
          Trusted By Industry Leaders & Universities
        </h2>
        <div className="overflow-hidden relative">
          <div className="absolute left-0 top-0 bottom-0 w-20 bg-gradient-to-r from-zinc-950 to-transparent z-10"></div>
          <div className="absolute right-0 top-0 bottom-0 w-20 bg-gradient-to-l from-zinc-950 to-transparent z-10"></div>

          <div className="flex animate-marquee gap-16 items-center">
            {[...logos, ...logos, ...logos].map((logo, index) => (
              <div
                key={index}
                className="flex-shrink-0 grayscale hover:grayscale-0 opacity-50 hover:opacity-100 transition-all duration-300"
              >
                <img src={logo.src || "/placeholder.svg"} alt={logo.name} className="h-10 w-auto object-contain" />
              </div>
            ))}
          </div>
        </div>
      </div>
    </section>
  )
}
